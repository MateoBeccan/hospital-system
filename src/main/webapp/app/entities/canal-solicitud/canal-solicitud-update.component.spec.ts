import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import CanalSolicitudUpdate from './canal-solicitud-update.vue';
import CanalSolicitudService from './canal-solicitud.service';

type CanalSolicitudUpdateComponentType = InstanceType<typeof CanalSolicitudUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const canalSolicitudSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CanalSolicitudUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CanalSolicitud Management Update Component', () => {
    let comp: CanalSolicitudUpdateComponentType;
    let canalSolicitudServiceStub: SinonStubbedInstance<CanalSolicitudService>;

    beforeEach(() => {
      route = {};
      canalSolicitudServiceStub = sinon.createStubInstance<CanalSolicitudService>(CanalSolicitudService);
      canalSolicitudServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          canalSolicitudService: () => canalSolicitudServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CanalSolicitudUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.canalSolicitud = canalSolicitudSample;
        canalSolicitudServiceStub.update.resolves(canalSolicitudSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(canalSolicitudServiceStub.update.calledWith(canalSolicitudSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        canalSolicitudServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CanalSolicitudUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.canalSolicitud = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(canalSolicitudServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        canalSolicitudServiceStub.find.resolves(canalSolicitudSample);
        canalSolicitudServiceStub.retrieve.resolves([canalSolicitudSample]);

        // WHEN
        route = {
          params: {
            canalSolicitudId: `${canalSolicitudSample.id}`,
          },
        };
        const wrapper = shallowMount(CanalSolicitudUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.canalSolicitud).toMatchObject(canalSolicitudSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        canalSolicitudServiceStub.find.resolves(canalSolicitudSample);
        const wrapper = shallowMount(CanalSolicitudUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
