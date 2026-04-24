import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import GrupoSanguineoDetails from './grupo-sanguineo-details.vue';
import GrupoSanguineoService from './grupo-sanguineo.service';

type GrupoSanguineoDetailsComponentType = InstanceType<typeof GrupoSanguineoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const grupoSanguineoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('GrupoSanguineo Management Detail Component', () => {
    let grupoSanguineoServiceStub: SinonStubbedInstance<GrupoSanguineoService>;
    let mountOptions: MountingOptions<GrupoSanguineoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      grupoSanguineoServiceStub = sinon.createStubInstance<GrupoSanguineoService>(GrupoSanguineoService);

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
          grupoSanguineoService: () => grupoSanguineoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        grupoSanguineoServiceStub.find.resolves(grupoSanguineoSample);
        route = {
          params: {
            grupoSanguineoId: `${123}`,
          },
        };
        const wrapper = shallowMount(GrupoSanguineoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.grupoSanguineo).toMatchObject(grupoSanguineoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        grupoSanguineoServiceStub.find.resolves(grupoSanguineoSample);
        const wrapper = shallowMount(GrupoSanguineoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
