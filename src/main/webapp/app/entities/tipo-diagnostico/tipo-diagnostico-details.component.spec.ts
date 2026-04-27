import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TipoDiagnosticoDetails from './tipo-diagnostico-details.vue';
import TipoDiagnosticoService from './tipo-diagnostico.service';

type TipoDiagnosticoDetailsComponentType = InstanceType<typeof TipoDiagnosticoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoDiagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('TipoDiagnostico Management Detail Component', () => {
    let tipoDiagnosticoServiceStub: SinonStubbedInstance<TipoDiagnosticoService>;
    let mountOptions: MountingOptions<TipoDiagnosticoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tipoDiagnosticoServiceStub = sinon.createStubInstance<TipoDiagnosticoService>(TipoDiagnosticoService);

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
          tipoDiagnosticoService: () => tipoDiagnosticoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tipoDiagnosticoServiceStub.find.resolves(tipoDiagnosticoSample);
        route = {
          params: {
            tipoDiagnosticoId: `${123}`,
          },
        };
        const wrapper = shallowMount(TipoDiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tipoDiagnostico).toMatchObject(tipoDiagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoDiagnosticoServiceStub.find.resolves(tipoDiagnosticoSample);
        const wrapper = shallowMount(TipoDiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
