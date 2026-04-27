import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoDiagnosticoUpdate from './estado-diagnostico-update.vue';
import EstadoDiagnosticoService from './estado-diagnostico.service';

type EstadoDiagnosticoUpdateComponentType = InstanceType<typeof EstadoDiagnosticoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoDiagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EstadoDiagnosticoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('EstadoDiagnostico Management Update Component', () => {
    let comp: EstadoDiagnosticoUpdateComponentType;
    let estadoDiagnosticoServiceStub: SinonStubbedInstance<EstadoDiagnosticoService>;

    beforeEach(() => {
      route = {};
      estadoDiagnosticoServiceStub = sinon.createStubInstance<EstadoDiagnosticoService>(EstadoDiagnosticoService);
      estadoDiagnosticoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          estadoDiagnosticoService: () => estadoDiagnosticoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EstadoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoDiagnostico = estadoDiagnosticoSample;
        estadoDiagnosticoServiceStub.update.resolves(estadoDiagnosticoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoDiagnosticoServiceStub.update.calledWith(estadoDiagnosticoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        estadoDiagnosticoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EstadoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoDiagnostico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoDiagnosticoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        estadoDiagnosticoServiceStub.find.resolves(estadoDiagnosticoSample);
        estadoDiagnosticoServiceStub.retrieve.resolves([estadoDiagnosticoSample]);

        // WHEN
        route = {
          params: {
            estadoDiagnosticoId: `${estadoDiagnosticoSample.id}`,
          },
        };
        const wrapper = shallowMount(EstadoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.estadoDiagnostico).toMatchObject(estadoDiagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoDiagnosticoServiceStub.find.resolves(estadoDiagnosticoSample);
        const wrapper = shallowMount(EstadoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
