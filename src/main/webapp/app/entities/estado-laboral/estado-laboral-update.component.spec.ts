import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import EstadoLaboralUpdate from './estado-laboral-update.vue';
import EstadoLaboralService from './estado-laboral.service';

type EstadoLaboralUpdateComponentType = InstanceType<typeof EstadoLaboralUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const estadoLaboralSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<EstadoLaboralUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('EstadoLaboral Management Update Component', () => {
    let comp: EstadoLaboralUpdateComponentType;
    let estadoLaboralServiceStub: SinonStubbedInstance<EstadoLaboralService>;

    beforeEach(() => {
      route = {};
      estadoLaboralServiceStub = sinon.createStubInstance<EstadoLaboralService>(EstadoLaboralService);
      estadoLaboralServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          estadoLaboralService: () => estadoLaboralServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(EstadoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoLaboral = estadoLaboralSample;
        estadoLaboralServiceStub.update.resolves(estadoLaboralSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoLaboralServiceStub.update.calledWith(estadoLaboralSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        estadoLaboralServiceStub.create.resolves(entity);
        const wrapper = shallowMount(EstadoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.estadoLaboral = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoLaboralServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        estadoLaboralServiceStub.find.resolves(estadoLaboralSample);
        estadoLaboralServiceStub.retrieve.resolves([estadoLaboralSample]);

        // WHEN
        route = {
          params: {
            estadoLaboralId: `${estadoLaboralSample.id}`,
          },
        };
        const wrapper = shallowMount(EstadoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.estadoLaboral).toMatchObject(estadoLaboralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        estadoLaboralServiceStub.find.resolves(estadoLaboralSample);
        const wrapper = shallowMount(EstadoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
