import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import FactorRhDetails from './factor-rh-details.vue';
import FactorRhService from './factor-rh.service';

type FactorRhDetailsComponentType = InstanceType<typeof FactorRhDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const factorRhSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FactorRh Management Detail Component', () => {
    let factorRhServiceStub: SinonStubbedInstance<FactorRhService>;
    let mountOptions: MountingOptions<FactorRhDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      factorRhServiceStub = sinon.createStubInstance<FactorRhService>(FactorRhService);

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
          factorRhService: () => factorRhServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        factorRhServiceStub.find.resolves(factorRhSample);
        route = {
          params: {
            factorRhId: `${123}`,
          },
        };
        const wrapper = shallowMount(FactorRhDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.factorRh).toMatchObject(factorRhSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        factorRhServiceStub.find.resolves(factorRhSample);
        const wrapper = shallowMount(FactorRhDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
