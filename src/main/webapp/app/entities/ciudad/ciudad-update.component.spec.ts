import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ProvinciaService from '@/entities/provincia/provincia.service';
import AlertService from '@/shared/alert/alert.service';

import CiudadUpdate from './ciudad-update.vue';
import CiudadService from './ciudad.service';

type CiudadUpdateComponentType = InstanceType<typeof CiudadUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const ciudadSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CiudadUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Ciudad Management Update Component', () => {
    let comp: CiudadUpdateComponentType;
    let ciudadServiceStub: SinonStubbedInstance<CiudadService>;

    beforeEach(() => {
      route = {};
      ciudadServiceStub = sinon.createStubInstance<CiudadService>(CiudadService);
      ciudadServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          ciudadService: () => ciudadServiceStub,
          provinciaService: () =>
            sinon.createStubInstance<ProvinciaService>(ProvinciaService, {
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
        const wrapper = shallowMount(CiudadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.ciudad = ciudadSample;
        ciudadServiceStub.update.resolves(ciudadSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ciudadServiceStub.update.calledWith(ciudadSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        ciudadServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CiudadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.ciudad = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(ciudadServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        ciudadServiceStub.find.resolves(ciudadSample);
        ciudadServiceStub.retrieve.resolves([ciudadSample]);

        // WHEN
        route = {
          params: {
            ciudadId: `${ciudadSample.id}`,
          },
        };
        const wrapper = shallowMount(CiudadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.ciudad).toMatchObject(ciudadSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        ciudadServiceStub.find.resolves(ciudadSample);
        const wrapper = shallowMount(CiudadUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
