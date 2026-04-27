import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoTratamientoUpdate from './estado-tratamiento-update.vue';
import EstadoTratamientoService from './estado-tratamiento.service';

type EstadoTratamientoUpdateComponentType = InstanceType<typeof EstadoTratamientoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoTratamientoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EstadoTratamientoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('EstadoTratamiento Management Update Component', () => {
    let comp: EstadoTratamientoUpdateComponentType;
    let estadoTratamientoServiceStub: SinonStubbedInstance<EstadoTratamientoService>;

    beforeEach(() => {
      route = {};
      estadoTratamientoServiceStub = sinon.createStubInstance<EstadoTratamientoService>(EstadoTratamientoService);
      estadoTratamientoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          estadoTratamientoService: () => estadoTratamientoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EstadoTratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoTratamiento = estadoTratamientoSample;
        estadoTratamientoServiceStub.update.resolves(estadoTratamientoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoTratamientoServiceStub.update.calledWith(estadoTratamientoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        estadoTratamientoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EstadoTratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoTratamiento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoTratamientoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        estadoTratamientoServiceStub.find.resolves(estadoTratamientoSample);
        estadoTratamientoServiceStub.retrieve.resolves([estadoTratamientoSample]);

        // WHEN
        route = {
          params: {
            estadoTratamientoId: `${estadoTratamientoSample.id}`,
          },
        };
        const wrapper = shallowMount(EstadoTratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.estadoTratamiento).toMatchObject(estadoTratamientoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoTratamientoServiceStub.find.resolves(estadoTratamientoSample);
        const wrapper = shallowMount(EstadoTratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
