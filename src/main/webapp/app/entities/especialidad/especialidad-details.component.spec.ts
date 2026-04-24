import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EspecialidadDetails from './especialidad-details.vue';
import EspecialidadService from './especialidad.service';

type EspecialidadDetailsComponentType = InstanceType<typeof EspecialidadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const especialidadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Especialidad Management Detail Component', () => {
    let especialidadServiceStub: SinonStubbedInstance<EspecialidadService>;
    let mountOptions: MountingOptions<EspecialidadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      especialidadServiceStub = sinon.createStubInstance<EspecialidadService>(EspecialidadService);

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
          especialidadService: () => especialidadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        especialidadServiceStub.find.resolves(especialidadSample);
        route = {
          params: {
            especialidadId: `${123}`,
          },
        };
        const wrapper = shallowMount(EspecialidadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.especialidad).toMatchObject(especialidadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        especialidadServiceStub.find.resolves(especialidadSample);
        const wrapper = shallowMount(EspecialidadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
