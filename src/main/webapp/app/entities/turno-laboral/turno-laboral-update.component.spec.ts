import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TurnoLaboralUpdate from './turno-laboral-update.vue';
import TurnoLaboralService from './turno-laboral.service';

type TurnoLaboralUpdateComponentType = InstanceType<typeof TurnoLaboralUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const turnoLaboralSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<TurnoLaboralUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('TurnoLaboral Management Update Component', () => {
    let comp: TurnoLaboralUpdateComponentType;
    let turnoLaboralServiceStub: SinonStubbedInstance<TurnoLaboralService>;

    beforeEach(() => {
      route = {};
      turnoLaboralServiceStub = sinon.createStubInstance<TurnoLaboralService>(TurnoLaboralService);
      turnoLaboralServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          turnoLaboralService: () => turnoLaboralServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(TurnoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.turnoLaboral = turnoLaboralSample;
        turnoLaboralServiceStub.update.resolves(turnoLaboralSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.update.calledWith(turnoLaboralSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        turnoLaboralServiceStub.create.resolves(entity);
        const wrapper = shallowMount(TurnoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.turnoLaboral = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        turnoLaboralServiceStub.find.resolves(turnoLaboralSample);
        turnoLaboralServiceStub.retrieve.resolves([turnoLaboralSample]);

        // WHEN
        route = {
          params: {
            turnoLaboralId: `${turnoLaboralSample.id}`,
          },
        };
        const wrapper = shallowMount(TurnoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.turnoLaboral).toMatchObject(turnoLaboralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        turnoLaboralServiceStub.find.resolves(turnoLaboralSample);
        const wrapper = shallowMount(TurnoLaboralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
