import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EmpleadoDetails from './empleado-details.vue';
import EmpleadoService from './empleado.service';

type EmpleadoDetailsComponentType = InstanceType<typeof EmpleadoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const empleadoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Empleado Management Detail Component', () => {
    let empleadoServiceStub: SinonStubbedInstance<EmpleadoService>;
    let mountOptions: MountingOptions<EmpleadoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      empleadoServiceStub = sinon.createStubInstance<EmpleadoService>(EmpleadoService);

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
          empleadoService: () => empleadoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        empleadoServiceStub.find.resolves(empleadoSample);
        route = {
          params: {
            empleadoId: `${123}`,
          },
        };
        const wrapper = shallowMount(EmpleadoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.empleado).toMatchObject(empleadoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        empleadoServiceStub.find.resolves(empleadoSample);
        const wrapper = shallowMount(EmpleadoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
