import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CiudadService from '@/entities/ciudad/ciudad.service';
import SexoService from '@/entities/sexo/sexo.service';
import TipoDocumentoService from '@/entities/tipo-documento/tipo-documento.service';
import AlertService from '@/shared/alert/alert.service';

import PersonaUpdate from './persona-update.vue';
import PersonaService from './persona.service';

type PersonaUpdateComponentType = InstanceType<typeof PersonaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const personaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PersonaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Persona Management Update Component', () => {
    let comp: PersonaUpdateComponentType;
    let personaServiceStub: SinonStubbedInstance<PersonaService>;

    beforeEach(() => {
      route = {};
      personaServiceStub = sinon.createStubInstance<PersonaService>(PersonaService);
      personaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          personaService: () => personaServiceStub,
          tipoDocumentoService: () =>
            sinon.createStubInstance<TipoDocumentoService>(TipoDocumentoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          sexoService: () =>
            sinon.createStubInstance<SexoService>(SexoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          ciudadService: () =>
            sinon.createStubInstance<CiudadService>(CiudadService, {
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
        const wrapper = shallowMount(PersonaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.persona = personaSample;
        personaServiceStub.update.resolves(personaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personaServiceStub.update.calledWith(personaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        personaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PersonaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.persona = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(personaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        personaServiceStub.find.resolves(personaSample);
        personaServiceStub.retrieve.resolves([personaSample]);

        // WHEN
        route = {
          params: {
            personaId: `${personaSample.id}`,
          },
        };
        const wrapper = shallowMount(PersonaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.persona).toMatchObject(personaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        personaServiceStub.find.resolves(personaSample);
        const wrapper = shallowMount(PersonaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
