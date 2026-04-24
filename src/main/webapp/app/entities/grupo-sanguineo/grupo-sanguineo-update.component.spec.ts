import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import GrupoSanguineoUpdate from './grupo-sanguineo-update.vue';
import GrupoSanguineoService from './grupo-sanguineo.service';

type GrupoSanguineoUpdateComponentType = InstanceType<typeof GrupoSanguineoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const grupoSanguineoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<GrupoSanguineoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('GrupoSanguineo Management Update Component', () => {
    let comp: GrupoSanguineoUpdateComponentType;
    let grupoSanguineoServiceStub: SinonStubbedInstance<GrupoSanguineoService>;

    beforeEach(() => {
      route = {};
      grupoSanguineoServiceStub = sinon.createStubInstance<GrupoSanguineoService>(GrupoSanguineoService);
      grupoSanguineoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          grupoSanguineoService: () => grupoSanguineoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(GrupoSanguineoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.grupoSanguineo = grupoSanguineoSample;
        grupoSanguineoServiceStub.update.resolves(grupoSanguineoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(grupoSanguineoServiceStub.update.calledWith(grupoSanguineoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        grupoSanguineoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(GrupoSanguineoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.grupoSanguineo = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(grupoSanguineoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        grupoSanguineoServiceStub.find.resolves(grupoSanguineoSample);
        grupoSanguineoServiceStub.retrieve.resolves([grupoSanguineoSample]);

        // WHEN
        route = {
          params: {
            grupoSanguineoId: `${grupoSanguineoSample.id}`,
          },
        };
        const wrapper = shallowMount(GrupoSanguineoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.grupoSanguineo).toMatchObject(grupoSanguineoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        grupoSanguineoServiceStub.find.resolves(grupoSanguineoSample);
        const wrapper = shallowMount(GrupoSanguineoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
