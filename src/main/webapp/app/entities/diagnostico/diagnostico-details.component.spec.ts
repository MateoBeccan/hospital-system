import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import DiagnosticoDetails from './diagnostico-details.vue';
import DiagnosticoService from './diagnostico.service';

type DiagnosticoDetailsComponentType = InstanceType<typeof DiagnosticoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const diagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Diagnostico Management Detail Component', () => {
    let diagnosticoServiceStub: SinonStubbedInstance<DiagnosticoService>;
    let mountOptions: MountingOptions<DiagnosticoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      diagnosticoServiceStub = sinon.createStubInstance<DiagnosticoService>(DiagnosticoService);

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
          diagnosticoService: () => diagnosticoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        diagnosticoServiceStub.find.resolves(diagnosticoSample);
        route = {
          params: {
            diagnosticoId: `${123}`,
          },
        };
        const wrapper = shallowMount(DiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.diagnostico).toMatchObject(diagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        diagnosticoServiceStub.find.resolves(diagnosticoSample);
        const wrapper = shallowMount(DiagnosticoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
