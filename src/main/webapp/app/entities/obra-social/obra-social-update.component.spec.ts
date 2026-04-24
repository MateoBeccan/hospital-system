import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import ObraSocialUpdate from './obra-social-update.vue';
import ObraSocialService from './obra-social.service';

type ObraSocialUpdateComponentType = InstanceType<typeof ObraSocialUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const obraSocialSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ObraSocialUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ObraSocial Management Update Component', () => {
    let comp: ObraSocialUpdateComponentType;
    let obraSocialServiceStub: SinonStubbedInstance<ObraSocialService>;

    beforeEach(() => {
      route = {};
      obraSocialServiceStub = sinon.createStubInstance<ObraSocialService>(ObraSocialService);
      obraSocialServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          obraSocialService: () => obraSocialServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ObraSocialUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.obraSocial = obraSocialSample;
        obraSocialServiceStub.update.resolves(obraSocialSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(obraSocialServiceStub.update.calledWith(obraSocialSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        obraSocialServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ObraSocialUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.obraSocial = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(obraSocialServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        obraSocialServiceStub.find.resolves(obraSocialSample);
        obraSocialServiceStub.retrieve.resolves([obraSocialSample]);

        // WHEN
        route = {
          params: {
            obraSocialId: `${obraSocialSample.id}`,
          },
        };
        const wrapper = shallowMount(ObraSocialUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.obraSocial).toMatchObject(obraSocialSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        obraSocialServiceStub.find.resolves(obraSocialSample);
        const wrapper = shallowMount(ObraSocialUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
