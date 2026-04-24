import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ProvinciaDetails from './provincia-details.vue';
import ProvinciaService from './provincia.service';

type ProvinciaDetailsComponentType = InstanceType<typeof ProvinciaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const provinciaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Provincia Management Detail Component', () => {
    let provinciaServiceStub: SinonStubbedInstance<ProvinciaService>;
    let mountOptions: MountingOptions<ProvinciaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      provinciaServiceStub = sinon.createStubInstance<ProvinciaService>(ProvinciaService);

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
          provinciaService: () => provinciaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        provinciaServiceStub.find.resolves(provinciaSample);
        route = {
          params: {
            provinciaId: `${123}`,
          },
        };
        const wrapper = shallowMount(ProvinciaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.provincia).toMatchObject(provinciaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        provinciaServiceStub.find.resolves(provinciaSample);
        const wrapper = shallowMount(ProvinciaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
