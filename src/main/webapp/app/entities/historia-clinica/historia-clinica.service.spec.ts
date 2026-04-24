import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { HistoriaClinica } from '@/shared/model/historia-clinica.model';

import HistoriaClinicaService from './historia-clinica.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('HistoriaClinica Service', () => {
    let service: HistoriaClinicaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new HistoriaClinicaService();
      currentDate = new Date();
      elemDefault = new HistoriaClinica(
        123,
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        currentDate,
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaApertura: dayjs(currentDate).format(DATE_FORMAT),
          fechaUltimaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          fechaCierre: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a HistoriaClinica', async () => {
        const returnedFromService = {
          id: 123,
          fechaApertura: dayjs(currentDate).format(DATE_FORMAT),
          fechaUltimaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          fechaCierre: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaApertura: currentDate,
          fechaUltimaActualizacion: currentDate,
          fechaCierre: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a HistoriaClinica', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a HistoriaClinica', async () => {
        const returnedFromService = {
          numero: 'BBBBBB',
          fechaApertura: dayjs(currentDate).format(DATE_FORMAT),
          fechaUltimaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          antecedentesPersonales: 'BBBBBB',
          antecedentesFamiliares: 'BBBBBB',
          enfermedadesPrevias: 'BBBBBB',
          cirugiasPrevias: 'BBBBBB',
          alergias: 'BBBBBB',
          medicacionHabitual: 'BBBBBB',
          habitos: 'BBBBBB',
          observacionesGenerales: 'BBBBBB',
          activa: true,
          fechaCierre: dayjs(currentDate).format(DATE_FORMAT),
          motivoCierre: 'BBBBBB',
          ...elemDefault,
        };

        const expected = {
          fechaApertura: currentDate,
          fechaUltimaActualizacion: currentDate,
          fechaCierre: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a HistoriaClinica', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a HistoriaClinica', async () => {
        const patchObject = {
          numero: 'BBBBBB',
          fechaApertura: dayjs(currentDate).format(DATE_FORMAT),
          antecedentesPersonales: 'BBBBBB',
          antecedentesFamiliares: 'BBBBBB',
          cirugiasPrevias: 'BBBBBB',
          habitos: 'BBBBBB',
          fechaCierre: dayjs(currentDate).format(DATE_FORMAT),
          motivoCierre: 'BBBBBB',
          ...new HistoriaClinica(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          fechaApertura: currentDate,
          fechaUltimaActualizacion: currentDate,
          fechaCierre: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a HistoriaClinica', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of HistoriaClinica', async () => {
        const returnedFromService = {
          numero: 'BBBBBB',
          fechaApertura: dayjs(currentDate).format(DATE_FORMAT),
          fechaUltimaActualizacion: dayjs(currentDate).format(DATE_FORMAT),
          antecedentesPersonales: 'BBBBBB',
          antecedentesFamiliares: 'BBBBBB',
          enfermedadesPrevias: 'BBBBBB',
          cirugiasPrevias: 'BBBBBB',
          alergias: 'BBBBBB',
          medicacionHabitual: 'BBBBBB',
          habitos: 'BBBBBB',
          observacionesGenerales: 'BBBBBB',
          activa: true,
          fechaCierre: dayjs(currentDate).format(DATE_FORMAT),
          motivoCierre: 'BBBBBB',
          ...elemDefault,
        };
        const expected = {
          fechaApertura: currentDate,
          fechaUltimaActualizacion: currentDate,
          fechaCierre: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of HistoriaClinica', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a HistoriaClinica', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a HistoriaClinica', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
