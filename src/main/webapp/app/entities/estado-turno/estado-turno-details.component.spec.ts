import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoTurnoDetails from './estado-turno-details.vue';
import EstadoTurnoService from './estado-turno.service';

type EstadoTurnoDetailsComponentType = InstanceType<typeof EstadoTurnoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoTurnoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('EstadoTurno Management Detail Component', () => {
    let estadoTurnoServiceStub: SinonStubbedInstance<EstadoTurnoService>;
    let mountOptions: MountingOptions<EstadoTurnoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      estadoTurnoServiceStub = sinon.createStubInstance<EstadoTurnoService>(EstadoTurnoService);

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
          estadoTurnoService: () => estadoTurnoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        estadoTurnoServiceStub.find.resolves(estadoTurnoSample);
        route = {
          params: {
            estadoTurnoId: `${123}`,
          },
        };
        const wrapper = shallowMount(EstadoTurnoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.estadoTurno).toMatchObject(estadoTurnoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoTurnoServiceStub.find.resolves(estadoTurnoSample);
        const wrapper = shallowMount(EstadoTurnoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
