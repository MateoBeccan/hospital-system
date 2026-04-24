import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ObraSocialDetails from './obra-social-details.vue';
import ObraSocialService from './obra-social.service';

type ObraSocialDetailsComponentType = InstanceType<typeof ObraSocialDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const obraSocialSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ObraSocial Management Detail Component', () => {
    let obraSocialServiceStub: SinonStubbedInstance<ObraSocialService>;
    let mountOptions: MountingOptions<ObraSocialDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      obraSocialServiceStub = sinon.createStubInstance<ObraSocialService>(ObraSocialService);

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
          obraSocialService: () => obraSocialServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        obraSocialServiceStub.find.resolves(obraSocialSample);
        route = {
          params: {
            obraSocialId: `${123}`,
          },
        };
        const wrapper = shallowMount(ObraSocialDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.obraSocial).toMatchObject(obraSocialSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        obraSocialServiceStub.find.resolves(obraSocialSample);
        const wrapper = shallowMount(ObraSocialDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
