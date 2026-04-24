import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ContactoEmergenciaDetails from './contacto-emergencia-details.vue';
import ContactoEmergenciaService from './contacto-emergencia.service';

type ContactoEmergenciaDetailsComponentType = InstanceType<typeof ContactoEmergenciaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const contactoEmergenciaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ContactoEmergencia Management Detail Component', () => {
    let contactoEmergenciaServiceStub: SinonStubbedInstance<ContactoEmergenciaService>;
    let mountOptions: MountingOptions<ContactoEmergenciaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      contactoEmergenciaServiceStub = sinon.createStubInstance<ContactoEmergenciaService>(ContactoEmergenciaService);

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
          contactoEmergenciaService: () => contactoEmergenciaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        contactoEmergenciaServiceStub.find.resolves(contactoEmergenciaSample);
        route = {
          params: {
            contactoEmergenciaId: `${123}`,
          },
        };
        const wrapper = shallowMount(ContactoEmergenciaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.contactoEmergencia).toMatchObject(contactoEmergenciaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        contactoEmergenciaServiceStub.find.resolves(contactoEmergenciaSample);
        const wrapper = shallowMount(ContactoEmergenciaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
