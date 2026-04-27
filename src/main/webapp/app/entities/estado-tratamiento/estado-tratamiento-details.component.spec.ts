import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoTratamientoDetails from './estado-tratamiento-details.vue';
import EstadoTratamientoService from './estado-tratamiento.service';

type EstadoTratamientoDetailsComponentType = InstanceType<typeof EstadoTratamientoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoTratamientoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('EstadoTratamiento Management Detail Component', () => {
    let estadoTratamientoServiceStub: SinonStubbedInstance<EstadoTratamientoService>;
    let mountOptions: MountingOptions<EstadoTratamientoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      estadoTratamientoServiceStub = sinon.createStubInstance<EstadoTratamientoService>(EstadoTratamientoService);

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
          estadoTratamientoService: () => estadoTratamientoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        estadoTratamientoServiceStub.find.resolves(estadoTratamientoSample);
        route = {
          params: {
            estadoTratamientoId: `${123}`,
          },
        };
        const wrapper = shallowMount(EstadoTratamientoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.estadoTratamiento).toMatchObject(estadoTratamientoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoTratamientoServiceStub.find.resolves(estadoTratamientoSample);
        const wrapper = shallowMount(EstadoTratamientoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
