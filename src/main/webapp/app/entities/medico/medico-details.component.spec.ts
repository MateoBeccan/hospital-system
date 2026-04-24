import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import MedicoDetails from './medico-details.vue';
import MedicoService from './medico.service';

type MedicoDetailsComponentType = InstanceType<typeof MedicoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medicoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Medico Management Detail Component', () => {
    let medicoServiceStub: SinonStubbedInstance<MedicoService>;
    let mountOptions: MountingOptions<MedicoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      medicoServiceStub = sinon.createStubInstance<MedicoService>(MedicoService);

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
          medicoService: () => medicoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        medicoServiceStub.find.resolves(medicoSample);
        route = {
          params: {
            medicoId: `${123}`,
          },
        };
        const wrapper = shallowMount(MedicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.medico).toMatchObject(medicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medicoServiceStub.find.resolves(medicoSample);
        const wrapper = shallowMount(MedicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
