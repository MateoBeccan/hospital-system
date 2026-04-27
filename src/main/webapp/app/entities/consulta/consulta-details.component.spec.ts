import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ConsultaDetails from './consulta-details.vue';
import ConsultaService from './consulta.service';

type ConsultaDetailsComponentType = InstanceType<typeof ConsultaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const consultaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Consulta Management Detail Component', () => {
    let consultaServiceStub: SinonStubbedInstance<ConsultaService>;
    let mountOptions: MountingOptions<ConsultaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      consultaServiceStub = sinon.createStubInstance<ConsultaService>(ConsultaService);

      alertService = new AlertService({
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          consultaService: () => consultaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        consultaServiceStub.find.resolves(consultaSample);
        route = {
          params: {
            consultaId: `${123}`,
          },
        };
        const wrapper = shallowMount(ConsultaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.consulta).toMatchObject(consultaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        consultaServiceStub.find.resolves(consultaSample);
        const wrapper = shallowMount(ConsultaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
