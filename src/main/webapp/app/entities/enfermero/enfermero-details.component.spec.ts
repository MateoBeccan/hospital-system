import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EnfermeroDetails from './enfermero-details.vue';
import EnfermeroService from './enfermero.service';

type EnfermeroDetailsComponentType = InstanceType<typeof EnfermeroDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const enfermeroSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Enfermero Management Detail Component', () => {
    let enfermeroServiceStub: SinonStubbedInstance<EnfermeroService>;
    let mountOptions: MountingOptions<EnfermeroDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      enfermeroServiceStub = sinon.createStubInstance<EnfermeroService>(EnfermeroService);

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
          enfermeroService: () => enfermeroServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        enfermeroServiceStub.find.resolves(enfermeroSample);
        route = {
          params: {
            enfermeroId: `${123}`,
          },
        };
        const wrapper = shallowMount(EnfermeroDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.enfermero).toMatchObject(enfermeroSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        enfermeroServiceStub.find.resolves(enfermeroSample);
        const wrapper = shallowMount(EnfermeroDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
