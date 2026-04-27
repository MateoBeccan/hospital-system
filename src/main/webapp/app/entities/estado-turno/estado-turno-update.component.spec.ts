import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoTurnoUpdate from './estado-turno-update.vue';
import EstadoTurnoService from './estado-turno.service';

type EstadoTurnoUpdateComponentType = InstanceType<typeof EstadoTurnoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoTurnoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EstadoTurnoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('EstadoTurno Management Update Component', () => {
    let comp: EstadoTurnoUpdateComponentType;
    let estadoTurnoServiceStub: SinonStubbedInstance<EstadoTurnoService>;

    beforeEach(() => {
      route = {};
      estadoTurnoServiceStub = sinon.createStubInstance<EstadoTurnoService>(EstadoTurnoService);
      estadoTurnoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          estadoTurnoService: () => estadoTurnoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EstadoTurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoTurno = estadoTurnoSample;
        estadoTurnoServiceStub.update.resolves(estadoTurnoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoTurnoServiceStub.update.calledWith(estadoTurnoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        estadoTurnoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EstadoTurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoTurno = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoTurnoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        estadoTurnoServiceStub.find.resolves(estadoTurnoSample);
        estadoTurnoServiceStub.retrieve.resolves([estadoTurnoSample]);

        // WHEN
        route = {
          params: {
            estadoTurnoId: `${estadoTurnoSample.id}`,
          },
        };
        const wrapper = shallowMount(EstadoTurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.estadoTurno).toMatchObject(estadoTurnoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoTurnoServiceStub.find.resolves(estadoTurnoSample);
        const wrapper = shallowMount(EstadoTurnoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
