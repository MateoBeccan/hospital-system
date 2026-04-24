import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TurnoLaboralDetails from './turno-laboral-details.vue';
import TurnoLaboralService from './turno-laboral.service';

type TurnoLaboralDetailsComponentType = InstanceType<typeof TurnoLaboralDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const turnoLaboralSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TurnoLaboral Management Detail Component', () => {
    let turnoLaboralServiceStub: SinonStubbedInstance<TurnoLaboralService>;
    let mountOptions: MountingOptions<TurnoLaboralDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      turnoLaboralServiceStub = sinon.createStubInstance<TurnoLaboralService>(TurnoLaboralService);

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
          turnoLaboralService: () => turnoLaboralServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        turnoLaboralServiceStub.find.resolves(turnoLaboralSample);
        route = {
          params: {
            turnoLaboralId: `${123}`,
          },
        };
        const wrapper = shallowMount(TurnoLaboralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.turnoLaboral).toMatchObject(turnoLaboralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        turnoLaboralServiceStub.find.resolves(turnoLaboralSample);
        const wrapper = shallowMount(TurnoLaboralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
