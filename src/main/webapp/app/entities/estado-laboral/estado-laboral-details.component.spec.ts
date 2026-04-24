import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoLaboralDetails from './estado-laboral-details.vue';
import EstadoLaboralService from './estado-laboral.service';

type EstadoLaboralDetailsComponentType = InstanceType<typeof EstadoLaboralDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoLaboralSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('EstadoLaboral Management Detail Component', () => {
    let estadoLaboralServiceStub: SinonStubbedInstance<EstadoLaboralService>;
    let mountOptions: MountingOptions<EstadoLaboralDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      estadoLaboralServiceStub = sinon.createStubInstance<EstadoLaboralService>(EstadoLaboralService);

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
          estadoLaboralService: () => estadoLaboralServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        estadoLaboralServiceStub.find.resolves(estadoLaboralSample);
        route = {
          params: {
            estadoLaboralId: `${123}`,
          },
        };
        const wrapper = shallowMount(EstadoLaboralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.estadoLaboral).toMatchObject(estadoLaboralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoLaboralServiceStub.find.resolves(estadoLaboralSample);
        const wrapper = shallowMount(EstadoLaboralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
