import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EspecialidadUpdate from './especialidad-update.vue';
import EspecialidadService from './especialidad.service';

type EspecialidadUpdateComponentType = InstanceType<typeof EspecialidadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const especialidadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EspecialidadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Especialidad Management Update Component', () => {
    let comp: EspecialidadUpdateComponentType;
    let especialidadServiceStub: SinonStubbedInstance<EspecialidadService>;

    beforeEach(() => {
      route = {};
      especialidadServiceStub = sinon.createStubInstance<EspecialidadService>(EspecialidadService);
      especialidadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          especialidadService: () => especialidadServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EspecialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.especialidad = especialidadSample;
        especialidadServiceStub.update.resolves(especialidadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(especialidadServiceStub.update.calledWith(especialidadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        especialidadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EspecialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.especialidad = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(especialidadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        especialidadServiceStub.find.resolves(especialidadSample);
        especialidadServiceStub.retrieve.resolves([especialidadSample]);

        // WHEN
        route = {
          params: {
            especialidadId: `${especialidadSample.id}`,
          },
        };
        const wrapper = shallowMount(EspecialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.especialidad).toMatchObject(especialidadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        especialidadServiceStub.find.resolves(especialidadSample);
        const wrapper = shallowMount(EspecialidadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
