import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CargoUpdate from './cargo-update.vue';
import CargoService from './cargo.service';

type CargoUpdateComponentType = InstanceType<typeof CargoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CargoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cargo Management Update Component', () => {
    let comp: CargoUpdateComponentType;
    let cargoServiceStub: SinonStubbedInstance<CargoService>;

    beforeEach(() => {
      route = {};
      cargoServiceStub = sinon.createStubInstance<CargoService>(CargoService);
      cargoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cargoService: () => cargoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CargoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargo = cargoSample;
        cargoServiceStub.update.resolves(cargoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoServiceStub.update.calledWith(cargoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cargoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CargoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cargo = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cargoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cargoServiceStub.find.resolves(cargoSample);
        cargoServiceStub.retrieve.resolves([cargoSample]);

        // WHEN
        route = {
          params: {
            cargoId: `${cargoSample.id}`,
          },
        };
        const wrapper = shallowMount(CargoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cargo).toMatchObject(cargoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoServiceStub.find.resolves(cargoSample);
        const wrapper = shallowMount(CargoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
