import { beforeEach, describe, expect, it, vitest } from 'vitest';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';

import TurnoLaboralService from './turno-laboral.service';
import TurnoLaboral from './turno-laboral.vue';

type TurnoLaboralComponentType = InstanceType<typeof TurnoLaboral>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('TurnoLaboral Management Component', () => {
    let turnoLaboralServiceStub: SinonStubbedInstance<TurnoLaboralService>;
    let mountOptions: MountingOptions<TurnoLaboralComponentType>['global'];

    beforeEach(() => {
      turnoLaboralServiceStub = sinon.createStubInstance<TurnoLaboralService>(TurnoLaboralService);
      turnoLaboralServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        toast: {
          show: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          turnoLaboralService: () => turnoLaboralServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        turnoLaboralServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(TurnoLaboral, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.turnoLaborals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(TurnoLaboral, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: TurnoLaboralComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(TurnoLaboral, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        turnoLaboralServiceStub.retrieve.reset();
        turnoLaboralServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        turnoLaboralServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.retrieve.called).toBeTruthy();
        expect(comp.turnoLaborals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(turnoLaboralServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        turnoLaboralServiceStub.retrieve.reset();
        turnoLaboralServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(turnoLaboralServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.turnoLaborals[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(turnoLaboralServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        turnoLaboralServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeTurnoLaboral();
        await comp.$nextTick(); // clear components

        // THEN
        expect(turnoLaboralServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(turnoLaboralServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
