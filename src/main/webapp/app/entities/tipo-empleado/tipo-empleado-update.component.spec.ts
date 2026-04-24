import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TipoEmpleadoUpdate from './tipo-empleado-update.vue';
import TipoEmpleadoService from './tipo-empleado.service';

type TipoEmpleadoUpdateComponentType = InstanceType<typeof TipoEmpleadoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoEmpleadoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TipoEmpleadoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TipoEmpleado Management Update Component', () => {
    let comp: TipoEmpleadoUpdateComponentType;
    let tipoEmpleadoServiceStub: SinonStubbedInstance<TipoEmpleadoService>;

    beforeEach(() => {
      route = {};
      tipoEmpleadoServiceStub = sinon.createStubInstance<TipoEmpleadoService>(TipoEmpleadoService);
      tipoEmpleadoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tipoEmpleadoService: () => tipoEmpleadoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TipoEmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoEmpleado = tipoEmpleadoSample;
        tipoEmpleadoServiceStub.update.resolves(tipoEmpleadoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoEmpleadoServiceStub.update.calledWith(tipoEmpleadoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tipoEmpleadoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TipoEmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoEmpleado = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoEmpleadoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tipoEmpleadoServiceStub.find.resolves(tipoEmpleadoSample);
        tipoEmpleadoServiceStub.retrieve.resolves([tipoEmpleadoSample]);

        // WHEN
        route = {
          params: {
            tipoEmpleadoId: `${tipoEmpleadoSample.id}`,
          },
        };
        const wrapper = shallowMount(TipoEmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tipoEmpleado).toMatchObject(tipoEmpleadoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoEmpleadoServiceStub.find.resolves(tipoEmpleadoSample);
        const wrapper = shallowMount(TipoEmpleadoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
