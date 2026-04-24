import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import SexoUpdate from './sexo-update.vue';
import SexoService from './sexo.service';

type SexoUpdateComponentType = InstanceType<typeof SexoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sexoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SexoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Sexo Management Update Component', () => {
    let comp: SexoUpdateComponentType;
    let sexoServiceStub: SinonStubbedInstance<SexoService>;

    beforeEach(() => {
      route = {};
      sexoServiceStub = sinon.createStubInstance<SexoService>(SexoService);
      sexoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          sexoService: () => sexoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SexoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sexo = sexoSample;
        sexoServiceStub.update.resolves(sexoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sexoServiceStub.update.calledWith(sexoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        sexoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SexoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sexo = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sexoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        sexoServiceStub.find.resolves(sexoSample);
        sexoServiceStub.retrieve.resolves([sexoSample]);

        // WHEN
        route = {
          params: {
            sexoId: `${sexoSample.id}`,
          },
        };
        const wrapper = shallowMount(SexoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.sexo).toMatchObject(sexoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sexoServiceStub.find.resolves(sexoSample);
        const wrapper = shallowMount(SexoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
