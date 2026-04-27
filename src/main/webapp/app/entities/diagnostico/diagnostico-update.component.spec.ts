import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ConsultaService from '@/entities/consulta/consulta.service';
import EstadoDiagnosticoService from '@/entities/estado-diagnostico/estado-diagnostico.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import TipoDiagnosticoService from '@/entities/tipo-diagnostico/tipo-diagnostico.service';
import AlertService from '@/shared/alert/alert.service';

import DiagnosticoUpdate from './diagnostico-update.vue';
import DiagnosticoService from './diagnostico.service';

type DiagnosticoUpdateComponentType = InstanceType<typeof DiagnosticoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const diagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<DiagnosticoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Diagnostico Management Update Component', () => {
    let comp: DiagnosticoUpdateComponentType;
    let diagnosticoServiceStub: SinonStubbedInstance<DiagnosticoService>;

    beforeEach(() => {
      route = {};
      diagnosticoServiceStub = sinon.createStubInstance<DiagnosticoService>(DiagnosticoService);
      diagnosticoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          diagnosticoService: () => diagnosticoServiceStub,
          consultaService: () =>
            sinon.createStubInstance<ConsultaService>(ConsultaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          medicoService: () =>
            sinon.createStubInstance<MedicoService>(MedicoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          tipoDiagnosticoService: () =>
            sinon.createStubInstance<TipoDiagnosticoService>(TipoDiagnosticoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          estadoDiagnosticoService: () =>
            sinon.createStubInstance<EstadoDiagnosticoService>(EstadoDiagnosticoService, {
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
        const wrapper = shallowMount(DiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.diagnostico = diagnosticoSample;
        diagnosticoServiceStub.update.resolves(diagnosticoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(diagnosticoServiceStub.update.calledWith(diagnosticoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        diagnosticoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(DiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.diagnostico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(diagnosticoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        diagnosticoServiceStub.find.resolves(diagnosticoSample);
        diagnosticoServiceStub.retrieve.resolves([diagnosticoSample]);

        // WHEN
        route = {
          params: {
            diagnosticoId: `${diagnosticoSample.id}`,
          },
        };
        const wrapper = shallowMount(DiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.diagnostico).toMatchObject(diagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        diagnosticoServiceStub.find.resolves(diagnosticoSample);
        const wrapper = shallowMount(DiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
