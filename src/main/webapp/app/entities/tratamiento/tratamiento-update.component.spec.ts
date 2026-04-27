import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import DiagnosticoService from '@/entities/diagnostico/diagnostico.service';
import EstadoTratamientoService from '@/entities/estado-tratamiento/estado-tratamiento.service';
import AlertService from '@/shared/alert/alert.service';

import TratamientoUpdate from './tratamiento-update.vue';
import TratamientoService from './tratamiento.service';

type TratamientoUpdateComponentType = InstanceType<typeof TratamientoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tratamientoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TratamientoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Tratamiento Management Update Component', () => {
    let comp: TratamientoUpdateComponentType;
    let tratamientoServiceStub: SinonStubbedInstance<TratamientoService>;

    beforeEach(() => {
      route = {};
      tratamientoServiceStub = sinon.createStubInstance<TratamientoService>(TratamientoService);
      tratamientoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tratamientoService: () => tratamientoServiceStub,
          diagnosticoService: () =>
            sinon.createStubInstance<DiagnosticoService>(DiagnosticoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          estadoTratamientoService: () =>
            sinon.createStubInstance<EstadoTratamientoService>(EstadoTratamientoService, {
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
        const wrapper = shallowMount(TratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tratamiento = tratamientoSample;
        tratamientoServiceStub.update.resolves(tratamientoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tratamientoServiceStub.update.calledWith(tratamientoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tratamientoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tratamiento = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tratamientoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tratamientoServiceStub.find.resolves(tratamientoSample);
        tratamientoServiceStub.retrieve.resolves([tratamientoSample]);

        // WHEN
        route = {
          params: {
            tratamientoId: `${tratamientoSample.id}`,
          },
        };
        const wrapper = shallowMount(TratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tratamiento).toMatchObject(tratamientoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tratamientoServiceStub.find.resolves(tratamientoSample);
        const wrapper = shallowMount(TratamientoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
