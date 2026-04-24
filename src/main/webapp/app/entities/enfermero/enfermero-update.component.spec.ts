import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import EmpleadoService from '@/entities/empleado/empleado.service';
import TurnoLaboralService from '@/entities/turno-laboral/turno-laboral.service';
import AlertService from '@/shared/alert/alert.service';

import EnfermeroUpdate from './enfermero-update.vue';
import EnfermeroService from './enfermero.service';

type EnfermeroUpdateComponentType = InstanceType<typeof EnfermeroUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const enfermeroSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EnfermeroUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Enfermero Management Update Component', () => {
    let comp: EnfermeroUpdateComponentType;
    let enfermeroServiceStub: SinonStubbedInstance<EnfermeroService>;

    beforeEach(() => {
      route = {};
      enfermeroServiceStub = sinon.createStubInstance<EnfermeroService>(EnfermeroService);
      enfermeroServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          enfermeroService: () => enfermeroServiceStub,
          empleadoService: () =>
            sinon.createStubInstance<EmpleadoService>(EmpleadoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          turnoLaboralService: () =>
            sinon.createStubInstance<TurnoLaboralService>(TurnoLaboralService, {
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
        const wrapper = shallowMount(EnfermeroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.enfermero = enfermeroSample;
        enfermeroServiceStub.update.resolves(enfermeroSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enfermeroServiceStub.update.calledWith(enfermeroSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        enfermeroServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EnfermeroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.enfermero = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enfermeroServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        enfermeroServiceStub.find.resolves(enfermeroSample);
        enfermeroServiceStub.retrieve.resolves([enfermeroSample]);

        // WHEN
        route = {
          params: {
            enfermeroId: `${enfermeroSample.id}`,
          },
        };
        const wrapper = shallowMount(EnfermeroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.enfermero).toMatchObject(enfermeroSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        enfermeroServiceStub.find.resolves(enfermeroSample);
        const wrapper = shallowMount(EnfermeroUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
