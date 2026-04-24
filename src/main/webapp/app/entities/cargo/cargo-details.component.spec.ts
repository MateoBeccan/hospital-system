import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CargoDetails from './cargo-details.vue';
import CargoService from './cargo.service';

type CargoDetailsComponentType = InstanceType<typeof CargoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Cargo Management Detail Component', () => {
    let cargoServiceStub: SinonStubbedInstance<CargoService>;
    let mountOptions: MountingOptions<CargoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cargoServiceStub = sinon.createStubInstance<CargoService>(CargoService);

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
          cargoService: () => cargoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoServiceStub.find.resolves(cargoSample);
        route = {
          params: {
            cargoId: `${123}`,
          },
        };
        const wrapper = shallowMount(CargoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cargo).toMatchObject(cargoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoServiceStub.find.resolves(cargoSample);
        const wrapper = shallowMount(CargoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
