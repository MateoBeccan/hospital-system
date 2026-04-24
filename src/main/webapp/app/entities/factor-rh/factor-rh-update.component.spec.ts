import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import FactorRhUpdate from './factor-rh-update.vue';
import FactorRhService from './factor-rh.service';

type FactorRhUpdateComponentType = InstanceType<typeof FactorRhUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const factorRhSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FactorRhUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FactorRh Management Update Component', () => {
    let comp: FactorRhUpdateComponentType;
    let factorRhServiceStub: SinonStubbedInstance<FactorRhService>;

    beforeEach(() => {
      route = {};
      factorRhServiceStub = sinon.createStubInstance<FactorRhService>(FactorRhService);
      factorRhServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          factorRhService: () => factorRhServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(FactorRhUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.factorRh = factorRhSample;
        factorRhServiceStub.update.resolves(factorRhSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(factorRhServiceStub.update.calledWith(factorRhSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        factorRhServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FactorRhUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.factorRh = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(factorRhServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        factorRhServiceStub.find.resolves(factorRhSample);
        factorRhServiceStub.retrieve.resolves([factorRhSample]);

        // WHEN
        route = {
          params: {
            factorRhId: `${factorRhSample.id}`,
          },
        };
        const wrapper = shallowMount(FactorRhUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.factorRh).toMatchObject(factorRhSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        factorRhServiceStub.find.resolves(factorRhSample);
        const wrapper = shallowMount(FactorRhUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
