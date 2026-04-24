import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SexoDetails from './sexo-details.vue';
import SexoService from './sexo.service';

type SexoDetailsComponentType = InstanceType<typeof SexoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sexoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Sexo Management Detail Component', () => {
    let sexoServiceStub: SinonStubbedInstance<SexoService>;
    let mountOptions: MountingOptions<SexoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      sexoServiceStub = sinon.createStubInstance<SexoService>(SexoService);

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
          sexoService: () => sexoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sexoServiceStub.find.resolves(sexoSample);
        route = {
          params: {
            sexoId: `${123}`,
          },
        };
        const wrapper = shallowMount(SexoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.sexo).toMatchObject(sexoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sexoServiceStub.find.resolves(sexoSample);
        const wrapper = shallowMount(SexoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
