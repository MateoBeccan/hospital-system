import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PersonaDetails from './persona-details.vue';
import PersonaService from './persona.service';

type PersonaDetailsComponentType = InstanceType<typeof PersonaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Persona Management Detail Component', () => {
    let personaServiceStub: SinonStubbedInstance<PersonaService>;
    let mountOptions: MountingOptions<PersonaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      personaServiceStub = sinon.createStubInstance<PersonaService>(PersonaService);

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
          personaService: () => personaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        personaServiceStub.find.resolves(personaSample);
        route = {
          params: {
            personaId: `${123}`,
          },
        };
        const wrapper = shallowMount(PersonaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.persona).toMatchObject(personaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personaServiceStub.find.resolves(personaSample);
        const wrapper = shallowMount(PersonaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
