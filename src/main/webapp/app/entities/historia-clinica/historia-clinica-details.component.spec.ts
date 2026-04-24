import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import HistoriaClinicaDetails from './historia-clinica-details.vue';
import HistoriaClinicaService from './historia-clinica.service';

type HistoriaClinicaDetailsComponentType = InstanceType<typeof HistoriaClinicaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const historiaClinicaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('HistoriaClinica Management Detail Component', () => {
    let historiaClinicaServiceStub: SinonStubbedInstance<HistoriaClinicaService>;
    let mountOptions: MountingOptions<HistoriaClinicaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      historiaClinicaServiceStub = sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService);

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
          historiaClinicaService: () => historiaClinicaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        historiaClinicaServiceStub.find.resolves(historiaClinicaSample);
        route = {
          params: {
            historiaClinicaId: `${123}`,
          },
        };
        const wrapper = shallowMount(HistoriaClinicaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.historiaClinica).toMatchObject(historiaClinicaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        historiaClinicaServiceStub.find.resolves(historiaClinicaSample);
        const wrapper = shallowMount(HistoriaClinicaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
