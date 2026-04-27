import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CanalSolicitudDetails from './canal-solicitud-details.vue';
import CanalSolicitudService from './canal-solicitud.service';

type CanalSolicitudDetailsComponentType = InstanceType<typeof CanalSolicitudDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const canalSolicitudSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CanalSolicitud Management Detail Component', () => {
    let canalSolicitudServiceStub: SinonStubbedInstance<CanalSolicitudService>;
    let mountOptions: MountingOptions<CanalSolicitudDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      canalSolicitudServiceStub = sinon.createStubInstance<CanalSolicitudService>(CanalSolicitudService);

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
          canalSolicitudService: () => canalSolicitudServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        canalSolicitudServiceStub.find.resolves(canalSolicitudSample);
        route = {
          params: {
            canalSolicitudId: `${123}`,
          },
        };
        const wrapper = shallowMount(CanalSolicitudDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.canalSolicitud).toMatchObject(canalSolicitudSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        canalSolicitudServiceStub.find.resolves(canalSolicitudSample);
        const wrapper = shallowMount(CanalSolicitudDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
