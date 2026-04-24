import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PacienteService from '@/entities/paciente/paciente.service';
import AlertService from '@/shared/alert/alert.service';

import HistoriaClinicaUpdate from './historia-clinica-update.vue';
import HistoriaClinicaService from './historia-clinica.service';

type HistoriaClinicaUpdateComponentType = InstanceType<typeof HistoriaClinicaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const historiaClinicaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<HistoriaClinicaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('HistoriaClinica Management Update Component', () => {
    let comp: HistoriaClinicaUpdateComponentType;
    let historiaClinicaServiceStub: SinonStubbedInstance<HistoriaClinicaService>;

    beforeEach(() => {
      route = {};
      historiaClinicaServiceStub = sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService);
      historiaClinicaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          historiaClinicaService: () => historiaClinicaServiceStub,
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
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
        const wrapper = shallowMount(HistoriaClinicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.historiaClinica = historiaClinicaSample;
        historiaClinicaServiceStub.update.resolves(historiaClinicaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historiaClinicaServiceStub.update.calledWith(historiaClinicaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        historiaClinicaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(HistoriaClinicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.historiaClinica = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(historiaClinicaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        historiaClinicaServiceStub.find.resolves(historiaClinicaSample);
        historiaClinicaServiceStub.retrieve.resolves([historiaClinicaSample]);

        // WHEN
        route = {
          params: {
            historiaClinicaId: `${historiaClinicaSample.id}`,
          },
        };
        const wrapper = shallowMount(HistoriaClinicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.historiaClinica).toMatchObject(historiaClinicaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        historiaClinicaServiceStub.find.resolves(historiaClinicaSample);
        const wrapper = shallowMount(HistoriaClinicaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
