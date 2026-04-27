import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { type RouteLocation } from 'vue-router';

import { type MountingOptions, shallowMount } from '@vue/test-utils';
import dayjs from 'dayjs';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ConsultaService from '@/entities/consulta/consulta.service';
import AlertService from '@/shared/alert/alert.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';

import SignosVitalesUpdate from './signos-vitales-update.vue';
import SignosVitalesService from './signos-vitales.service';

type SignosVitalesUpdateComponentType = InstanceType<typeof SignosVitalesUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const signosVitalesSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SignosVitalesUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SignosVitales Management Update Component', () => {
    let comp: SignosVitalesUpdateComponentType;
    let signosVitalesServiceStub: SinonStubbedInstance<SignosVitalesService>;

    beforeEach(() => {
      route = {};
      signosVitalesServiceStub = sinon.createStubInstance<SignosVitalesService>(SignosVitalesService);
      signosVitalesServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          signosVitalesService: () => signosVitalesServiceStub,
          consultaService: () =>
            sinon.createStubInstance<ConsultaService>(ConsultaService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(SignosVitalesUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(SignosVitalesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.signosVitales = signosVitalesSample;
        signosVitalesServiceStub.update.resolves(signosVitalesSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(signosVitalesServiceStub.update.calledWith(signosVitalesSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        signosVitalesServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SignosVitalesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.signosVitales = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(signosVitalesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        signosVitalesServiceStub.find.resolves(signosVitalesSample);
        signosVitalesServiceStub.retrieve.resolves([signosVitalesSample]);

        // WHEN
        route = {
          params: {
            signosVitalesId: `${signosVitalesSample.id}`,
          },
        };
        const wrapper = shallowMount(SignosVitalesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.signosVitales).toMatchObject(signosVitalesSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        signosVitalesServiceStub.find.resolves(signosVitalesSample);
        const wrapper = shallowMount(SignosVitalesUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
