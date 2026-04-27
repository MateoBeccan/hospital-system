import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CanalSolicitudService from '@/entities/canal-solicitud/canal-solicitud.service';
import EspecialidadService from '@/entities/especialidad/especialidad.service';
import EstadoTurnoService from '@/entities/estado-turno/estado-turno.service';
import MedicoService from '@/entities/medico/medico.service';
import PacienteService from '@/entities/paciente/paciente.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import TurnoUpdate from './turno-update.vue';
import TurnoService from './turno.service';

type TurnoUpdateComponentType = InstanceType<typeof TurnoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const turnoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TurnoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Turno Management Update Component', () => {
    let comp: TurnoUpdateComponentType;
    let turnoServiceStub: SinonStubbedInstance<TurnoService>;

    beforeEach(() => {
      route = {};
      turnoServiceStub = sinon.createStubInstance<TurnoService>(TurnoService);
      turnoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          turnoService: () => turnoServiceStub,
          pacienteService: () =>
            sinon.createStubInstance<PacienteService>(PacienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          medicoService: () =>
            sinon.createStubInstance<MedicoService>(MedicoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          especialidadService: () =>
            sinon.createStubInstance<EspecialidadService>(EspecialidadService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          estadoTurnoService: () =>
            sinon.createStubInstance<EstadoTurnoService>(EstadoTurnoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          canalSolicitudService: () =>
            sinon.createStubInstance<CanalSolicitudService>(CanalSolicitudService, {
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
        const wrapper = shallowMount(TurnoUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(TurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.turno = turnoSample;
        turnoServiceStub.update.resolves(turnoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(turnoServiceStub.update.calledWith(turnoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        turnoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.turno = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(turnoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        turnoServiceStub.find.resolves(turnoSample);
        turnoServiceStub.retrieve.resolves([turnoSample]);

        // WHEN
        route = {
          params: {
            turnoId: `${turnoSample.id}`,
          },
        };
        const wrapper = shallowMount(TurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.turno).toMatchObject(turnoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        turnoServiceStub.find.resolves(turnoSample);
        const wrapper = shallowMount(TurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
