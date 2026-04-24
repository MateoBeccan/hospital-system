import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PaisDetails from './pais-details.vue';
import PaisService from './pais.service';

type PaisDetailsComponentType = InstanceType<typeof PaisDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paisSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Pais Management Detail Component', () => {
    let paisServiceStub: SinonStubbedInstance<PaisService>;
    let mountOptions: MountingOptions<PaisDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      paisServiceStub = sinon.createStubInstance<PaisService>(PaisService);

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
          paisService: () => paisServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paisServiceStub.find.resolves(paisSample);
        route = {
          params: {
            paisId: `${123}`,
          },
        };
        const wrapper = shallowMount(PaisDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.pais).toMatchObject(paisSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paisServiceStub.find.resolves(paisSample);
        const wrapper = shallowMount(PaisDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
