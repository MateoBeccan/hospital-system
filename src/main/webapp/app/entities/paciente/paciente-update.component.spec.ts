import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import FactorRhService from '@/entities/factor-rh/factor-rh.service';
import GrupoSanguineoService from '@/entities/grupo-sanguineo/grupo-sanguineo.service';
import ObraSocialService from '@/entities/obra-social/obra-social.service';
import PersonaService from '@/entities/persona/persona.service';
import AlertService from '@/shared/alert/alert.service';

import PacienteUpdate from './paciente-update.vue';
import PacienteService from './paciente.service';

type PacienteUpdateComponentType = InstanceType<typeof PacienteUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PacienteUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Paciente Management Update Component', () => {
    let comp: PacienteUpdateComponentType;
    let pacienteServiceStub: SinonStubbedInstance<PacienteService>;

    beforeEach(() => {
      route = {};
      pacienteServiceStub = sinon.createStubInstance<PacienteService>(PacienteService);
      pacienteServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          pacienteService: () => pacienteServiceStub,
          personaService: () =>
            sinon.createStubInstance<PersonaService>(PersonaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          obraSocialService: () =>
            sinon.createStubInstance<ObraSocialService>(ObraSocialService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          grupoSanguineoService: () =>
            sinon.createStubInstance<GrupoSanguineoService>(GrupoSanguineoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          factorRhService: () =>
            sinon.createStubInstance<FactorRhService>(FactorRhService, {
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
        const wrapper = shallowMount(PacienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paciente = pacienteSample;
        pacienteServiceStub.update.resolves(pacienteSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteServiceStub.update.calledWith(pacienteSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        pacienteServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PacienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paciente = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pacienteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        pacienteServiceStub.find.resolves(pacienteSample);
        pacienteServiceStub.retrieve.resolves([pacienteSample]);

        // WHEN
        route = {
          params: {
            pacienteId: `${pacienteSample.id}`,
          },
        };
        const wrapper = shallowMount(PacienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paciente).toMatchObject(pacienteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteServiceStub.find.resolves(pacienteSample);
        const wrapper = shallowMount(PacienteUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
