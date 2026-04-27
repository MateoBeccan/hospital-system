import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TurnoDetails from './turno-details.vue';
import TurnoService from './turno.service';

type TurnoDetailsComponentType = InstanceType<typeof TurnoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const turnoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Turno Management Detail Component', () => {
    let turnoServiceStub: SinonStubbedInstance<TurnoService>;
    let mountOptions: MountingOptions<TurnoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      turnoServiceStub = sinon.createStubInstance<TurnoService>(TurnoService);

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
          turnoService: () => turnoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        turnoServiceStub.find.resolves(turnoSample);
        route = {
          params: {
            turnoId: `${123}`,
          },
        };
        const wrapper = shallowMount(TurnoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.turno).toMatchObject(turnoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        turnoServiceStub.find.resolves(turnoSample);
        const wrapper = shallowMount(TurnoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
