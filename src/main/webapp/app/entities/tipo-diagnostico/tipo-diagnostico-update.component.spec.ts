import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TipoDiagnosticoUpdate from './tipo-diagnostico-update.vue';
import TipoDiagnosticoService from './tipo-diagnostico.service';

type TipoDiagnosticoUpdateComponentType = InstanceType<typeof TipoDiagnosticoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const tipoDiagnosticoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TipoDiagnosticoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TipoDiagnostico Management Update Component', () => {
    let comp: TipoDiagnosticoUpdateComponentType;
    let tipoDiagnosticoServiceStub: SinonStubbedInstance<TipoDiagnosticoService>;

    beforeEach(() => {
      route = {};
      tipoDiagnosticoServiceStub = sinon.createStubInstance<TipoDiagnosticoService>(TipoDiagnosticoService);
      tipoDiagnosticoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          tipoDiagnosticoService: () => tipoDiagnosticoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TipoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoDiagnostico = tipoDiagnosticoSample;
        tipoDiagnosticoServiceStub.update.resolves(tipoDiagnosticoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.update.calledWith(tipoDiagnosticoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        tipoDiagnosticoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TipoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.tipoDiagnostico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(tipoDiagnosticoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        tipoDiagnosticoServiceStub.find.resolves(tipoDiagnosticoSample);
        tipoDiagnosticoServiceStub.retrieve.resolves([tipoDiagnosticoSample]);

        // WHEN
        route = {
          params: {
            tipoDiagnosticoId: `${tipoDiagnosticoSample.id}`,
          },
        };
        const wrapper = shallowMount(TipoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.tipoDiagnostico).toMatchObject(tipoDiagnosticoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        tipoDiagnosticoServiceStub.find.resolves(tipoDiagnosticoSample);
        const wrapper = shallowMount(TipoDiagnosticoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
