import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PaisService from '@/entities/pais/pais.service';
import AlertService from '@/shared/alert/alert.service';

import ProvinciaUpdate from './provincia-update.vue';
import ProvinciaService from './provincia.service';

type ProvinciaUpdateComponentType = InstanceType<typeof ProvinciaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const provinciaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ProvinciaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Provincia Management Update Component', () => {
    let comp: ProvinciaUpdateComponentType;
    let provinciaServiceStub: SinonStubbedInstance<ProvinciaService>;

    beforeEach(() => {
      route = {};
      provinciaServiceStub = sinon.createStubInstance<ProvinciaService>(ProvinciaService);
      provinciaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          provinciaService: () => provinciaServiceStub,
          paisService: () =>
            sinon.createStubInstance<PaisService>(PaisService, {
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
        const wrapper = shallowMount(ProvinciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.provincia = provinciaSample;
        provinciaServiceStub.update.resolves(provinciaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(provinciaServiceStub.update.calledWith(provinciaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        provinciaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ProvinciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.provincia = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(provinciaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        provinciaServiceStub.find.resolves(provinciaSample);
        provinciaServiceStub.retrieve.resolves([provinciaSample]);

        // WHEN
        route = {
          params: {
            provinciaId: `${provinciaSample.id}`,
          },
        };
        const wrapper = shallowMount(ProvinciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.provincia).toMatchObject(provinciaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        provinciaServiceStub.find.resolves(provinciaSample);
        const wrapper = shallowMount(ProvinciaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
