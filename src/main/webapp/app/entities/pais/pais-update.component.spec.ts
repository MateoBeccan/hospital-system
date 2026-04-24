import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import PaisUpdate from './pais-update.vue';
import PaisService from './pais.service';

type PaisUpdateComponentType = InstanceType<typeof PaisUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paisSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaisUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Pais Management Update Component', () => {
    let comp: PaisUpdateComponentType;
    let paisServiceStub: SinonStubbedInstance<PaisService>;

    beforeEach(() => {
      route = {};
      paisServiceStub = sinon.createStubInstance<PaisService>(PaisService);
      paisServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paisService: () => paisServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PaisUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pais = paisSample;
        paisServiceStub.update.resolves(paisSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisServiceStub.update.calledWith(paisSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paisServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaisUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.pais = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paisServiceStub.find.resolves(paisSample);
        paisServiceStub.retrieve.resolves([paisSample]);

        // WHEN
        route = {
          params: {
            paisId: `${paisSample.id}`,
          },
        };
        const wrapper = shallowMount(PaisUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.pais).toMatchObject(paisSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paisServiceStub.find.resolves(paisSample);
        const wrapper = shallowMount(PaisUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
