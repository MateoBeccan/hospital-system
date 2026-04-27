import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import TurnoService from '@/entities/turno/turno.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import ConsultaUpdate from './consulta-update.vue';
import ConsultaService from './consulta.service';

type ConsultaUpdateComponentType = InstanceType<typeof ConsultaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const consultaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ConsultaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Consulta Management Update Component', () => {
    let comp: ConsultaUpdateComponentType;
    let consultaServiceStub: SinonStubbedInstance<ConsultaService>;

    beforeEach(() => {
      route = {};
      consultaServiceStub = sinon.createStubInstance<ConsultaService>(ConsultaService);
      consultaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          consultaService: () => consultaServiceStub,
          turnoService: () =>
            sinon.createStubInstance<TurnoService>(TurnoService, {
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
          historiaClinicaService: () =>
            sinon.createStubInstance<HistoriaClinicaService>(HistoriaClinicaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(ConsultaUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ConsultaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.consulta = consultaSample;
        consultaServiceStub.update.resolves(consultaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(consultaServiceStub.update.calledWith(consultaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        consultaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ConsultaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.consulta = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(consultaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        consultaServiceStub.find.resolves(consultaSample);
        consultaServiceStub.retrieve.resolves([consultaSample]);

        // WHEN
        route = {
          params: {
            consultaId: `${consultaSample.id}`,
          },
        };
        const wrapper = shallowMount(ConsultaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.consulta).toMatchObject(consultaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        consultaServiceStub.find.resolves(consultaSample);
        const wrapper = shallowMount(ConsultaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
