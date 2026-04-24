import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PersonaService from '@/entities/persona/persona.service';
import AlertService from '@/shared/alert/alert.service';

import ContactoEmergenciaUpdate from './contacto-emergencia-update.vue';
import ContactoEmergenciaService from './contacto-emergencia.service';

type ContactoEmergenciaUpdateComponentType = InstanceType<typeof ContactoEmergenciaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const contactoEmergenciaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ContactoEmergenciaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ContactoEmergencia Management Update Component', () => {
    let comp: ContactoEmergenciaUpdateComponentType;
    let contactoEmergenciaServiceStub: SinonStubbedInstance<ContactoEmergenciaService>;

    beforeEach(() => {
      route = {};
      contactoEmergenciaServiceStub = sinon.createStubInstance<ContactoEmergenciaService>(ContactoEmergenciaService);
      contactoEmergenciaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          contactoEmergenciaService: () => contactoEmergenciaServiceStub,
          personaService: () =>
            sinon.createStubInstance<PersonaService>(PersonaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ContactoEmergenciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.contactoEmergencia = contactoEmergenciaSample;
        contactoEmergenciaServiceStub.update.resolves(contactoEmergenciaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contactoEmergenciaServiceStub.update.calledWith(contactoEmergenciaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        contactoEmergenciaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ContactoEmergenciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.contactoEmergencia = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(contactoEmergenciaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        contactoEmergenciaServiceStub.find.resolves(contactoEmergenciaSample);
        contactoEmergenciaServiceStub.retrieve.resolves([contactoEmergenciaSample]);

        // WHEN
        route = {
          params: {
            contactoEmergenciaId: `${contactoEmergenciaSample.id}`,
          },
        };
        const wrapper = shallowMount(ContactoEmergenciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.contactoEmergencia).toMatchObject(contactoEmergenciaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        contactoEmergenciaServiceStub.find.resolves(contactoEmergenciaSample);
        const wrapper = shallowMount(ContactoEmergenciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
