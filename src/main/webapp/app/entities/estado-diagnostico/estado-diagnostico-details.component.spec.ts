import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoDiagnosticoDetails from './estado-diagnostico-details.vue';
import EstadoDiagnosticoService from './estado-diagnostico.service';

type EstadoDiagnosticoDetailsComponentType = InstanceType<typeof EstadoDiagnosticoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoDiagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('EstadoDiagnostico Management Detail Component', () => {
    let estadoDiagnosticoServiceStub: SinonStubbedInstance<EstadoDiagnosticoService>;
    let mountOptions: MountingOptions<EstadoDiagnosticoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      estadoDiagnosticoServiceStub = sinon.createStubInstance<EstadoDiagnosticoService>(EstadoDiagnosticoService);

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
          estadoDiagnosticoService: () => estadoDiagnosticoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        estadoDiagnosticoServiceStub.find.resolves(estadoDiagnosticoSample);
        route = {
          params: {
            estadoDiagnosticoId: `${123}`,
          },
        };
        const wrapper = shallowMount(EstadoDiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.estadoDiagnostico).toMatchObject(estadoDiagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoDiagnosticoServiceStub.find.resolves(estadoDiagnosticoSample);
        const wrapper = shallowMount(EstadoDiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
