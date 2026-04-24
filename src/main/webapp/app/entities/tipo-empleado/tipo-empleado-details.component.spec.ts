import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TipoEmpleadoDetails from './tipo-empleado-details.vue';
import TipoEmpleadoService from './tipo-empleado.service';

type TipoEmpleadoDetailsComponentType = InstanceType<typeof TipoEmpleadoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoEmpleadoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TipoEmpleado Management Detail Component', () => {
    let tipoEmpleadoServiceStub: SinonStubbedInstance<TipoEmpleadoService>;
    let mountOptions: MountingOptions<TipoEmpleadoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tipoEmpleadoServiceStub = sinon.createStubInstance<TipoEmpleadoService>(TipoEmpleadoService);

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
          tipoEmpleadoService: () => tipoEmpleadoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoEmpleadoServiceStub.find.resolves(tipoEmpleadoSample);
        route = {
          params: {
            tipoEmpleadoId: `${123}`,
          },
        };
        const wrapper = shallowMount(TipoEmpleadoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tipoEmpleado).toMatchObject(tipoEmpleadoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoEmpleadoServiceStub.find.resolves(tipoEmpleadoSample);
        const wrapper = shallowMount(TipoEmpleadoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
