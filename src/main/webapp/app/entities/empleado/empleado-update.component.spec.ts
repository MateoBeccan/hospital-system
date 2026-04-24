import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import CargoService from '@/entities/cargo/cargo.service';
import EstadoLaboralService from '@/entities/estado-laboral/estado-laboral.service';
import PersonaService from '@/entities/persona/persona.service';
import TipoEmpleadoService from '@/entities/tipo-empleado/tipo-empleado.service';
import AlertService from '@/shared/alert/alert.service';

import EmpleadoUpdate from './empleado-update.vue';
import EmpleadoService from './empleado.service';

type EmpleadoUpdateComponentType = InstanceType<typeof EmpleadoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const empleadoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EmpleadoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Empleado Management Update Component', () => {
    let comp: EmpleadoUpdateComponentType;
    let empleadoServiceStub: SinonStubbedInstance<EmpleadoService>;

    beforeEach(() => {
      route = {};
      empleadoServiceStub = sinon.createStubInstance<EmpleadoService>(EmpleadoService);
      empleadoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          empleadoService: () => empleadoServiceStub,
          personaService: () =>
            sinon.createStubInstance<PersonaService>(PersonaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          tipoEmpleadoService: () =>
            sinon.createStubInstance<TipoEmpleadoService>(TipoEmpleadoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          estadoLaboralService: () =>
            sinon.createStubInstance<EstadoLaboralService>(EstadoLaboralService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          cargoService: () =>
            sinon.createStubInstance<CargoService>(CargoService, {
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
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.empleado = empleadoSample;
        empleadoServiceStub.update.resolves(empleadoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empleadoServiceStub.update.calledWith(empleadoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        empleadoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.empleado = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(empleadoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        empleadoServiceStub.find.resolves(empleadoSample);
        empleadoServiceStub.retrieve.resolves([empleadoSample]);

        // WHEN
        route = {
          params: {
            empleadoId: `${empleadoSample.id}`,
          },
        };
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.empleado).toMatchObject(empleadoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        empleadoServiceStub.find.resolves(empleadoSample);
        const wrapper = shallowMount(EmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
