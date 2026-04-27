import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TratamientoDetails from './tratamiento-details.vue';
import TratamientoService from './tratamiento.service';

type TratamientoDetailsComponentType = InstanceType<typeof TratamientoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tratamientoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Tratamiento Management Detail Component', () => {
    let tratamientoServiceStub: SinonStubbedInstance<TratamientoService>;
    let mountOptions: MountingOptions<TratamientoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      tratamientoServiceStub = sinon.createStubInstance<TratamientoService>(TratamientoService);

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
          tratamientoService: () => tratamientoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        tratamientoServiceStub.find.resolves(tratamientoSample);
        route = {
          params: {
            tratamientoId: `${123}`,
          },
        };
        const wrapper = shallowMount(TratamientoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.tratamiento).toMatchObject(tratamientoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tratamientoServiceStub.find.resolves(tratamientoSample);
        const wrapper = shallowMount(TratamientoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
