import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import AntecedenteClinicoDetails from './antecedente-clinico-details.vue';
import AntecedenteClinicoService from './antecedente-clinico.service';

type AntecedenteClinicoDetailsComponentType = InstanceType<typeof AntecedenteClinicoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const antecedenteClinicoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AntecedenteClinico Management Detail Component', () => {
    let antecedenteClinicoServiceStub: SinonStubbedInstance<AntecedenteClinicoService>;
    let mountOptions: MountingOptions<AntecedenteClinicoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      antecedenteClinicoServiceStub = sinon.createStubInstance<AntecedenteClinicoService>(AntecedenteClinicoService);

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
          antecedenteClinicoService: () => antecedenteClinicoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        antecedenteClinicoServiceStub.find.resolves(antecedenteClinicoSample);
        route = {
          params: {
            antecedenteClinicoId: `${123}`,
          },
        };
        const wrapper = shallowMount(AntecedenteClinicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.antecedenteClinico).toMatchObject(antecedenteClinicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        antecedenteClinicoServiceStub.find.resolves(antecedenteClinicoSample);
        const wrapper = shallowMount(AntecedenteClinicoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
