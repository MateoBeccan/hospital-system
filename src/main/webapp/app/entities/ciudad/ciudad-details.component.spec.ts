import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CiudadDetails from './ciudad-details.vue';
import CiudadService from './ciudad.service';

type CiudadDetailsComponentType = InstanceType<typeof CiudadDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const ciudadSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Ciudad Management Detail Component', () => {
    let ciudadServiceStub: SinonStubbedInstance<CiudadService>;
    let mountOptions: MountingOptions<CiudadDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      ciudadServiceStub = sinon.createStubInstance<CiudadService>(CiudadService);

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
          ciudadService: () => ciudadServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        ciudadServiceStub.find.resolves(ciudadSample);
        route = {
          params: {
            ciudadId: `${123}`,
          },
        };
        const wrapper = shallowMount(CiudadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.ciudad).toMatchObject(ciudadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        ciudadServiceStub.find.resolves(ciudadSample);
        const wrapper = shallowMount(CiudadDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
